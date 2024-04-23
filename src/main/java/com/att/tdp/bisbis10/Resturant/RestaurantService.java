package com.att.tdp.bisbis10.Resturant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.att.tdp.bisbis10.Dishes.DishRepository;
import com.att.tdp.bisbis10.Dishes.Dishes;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {

    private static RestaurantRepository restaurantRepository;
    private static DishRepository dishRepository;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository, DishRepository dishRepository) {
        this.restaurantRepository = restaurantRepository;
        this.dishRepository = dishRepository;
    }

    public List<Restaurant> getRestaurants() {
        return restaurantRepository.findAll();
    }

    public List<Restaurant> getRestaurantsByCuisine(String cuisine) {
        return restaurantRepository.findByCuisinesContaining(cuisine);
    }

    public Optional<Restaurant> getRestaurantbyId(Long restaurantId) {
        return restaurantRepository.findById(restaurantId);
    }

    public Restaurant addRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }


    @Transactional
    public static void deleteRestaurantAndDishes(Long restaurantId) throws NotFoundException {
        // Find the restaurant by ID
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
        if (optionalRestaurant.isPresent()) {
            Restaurant restaurant = optionalRestaurant.get();
            
            // Find all dishes associated with the restaurant
            List<Dishes> dishes = dishRepository.findByRestaurantId(restaurantId);

            // Delete all dishes
            dishRepository.deleteAll(dishes);

            // Delete the restaurant
            restaurantRepository.delete(restaurant);
        } else {
            // Handle case where restaurant with the given ID is not found
            throw new NotFoundException();
        }
    }

    public static Restaurant updateRestaurant(Long restaurantId, Restaurant updatedRestaurant) throws NotFoundException {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);
        if (optionalRestaurant.isPresent()) {
            Restaurant restaurant = optionalRestaurant.get();
            // Update the restaurant properties with the provided updatedRestaurant object
            if (updatedRestaurant.getName() != null) {
                restaurant.setName(updatedRestaurant.getName());
            }
            if (updatedRestaurant.getAverageRating() != 0) {
                restaurant.setAverageRating(updatedRestaurant.getAverageRating());
            }
            if (updatedRestaurant.isKosher() != restaurant.isKosher()) {
                restaurant.setKosher(updatedRestaurant.isKosher());
            }
            if (updatedRestaurant.getCuisines() != null) {
                restaurant.setCuisines(updatedRestaurant.getCuisines());
            }
            // Similarly, update other properties as needed
            // Save and return the updated restaurant
            return restaurantRepository.save(restaurant);
        } else {
            throw new NotFoundException();
        }
    }

}



package com.kxtract.data;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

import com.kxtract.data.dao.Subscription;

public interface SubscriptionRepository extends CrudRepository<Subscription, Integer>{
	  List<Subscription> findByUserId(String userId);

	  Subscription findById(int id);
}
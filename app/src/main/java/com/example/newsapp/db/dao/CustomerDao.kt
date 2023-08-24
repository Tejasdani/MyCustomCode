package com.example.newsapp.db.dao

import androidx.room.Dao

/**
 * Created by Tejas.Dani on 27,January,2023
 */

@Dao
interface CustomerDao {

   /* @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCustomer(customer: Customer)

    @Query("select * from customers order by customerId desc")
    suspend fun getAllCustomers() : List<Customer>*/
}
package com.example.newsapp.ui.dashboard.model

data class NewTestModel(
    var employeeList: List<List<Employee?>?>? = null
)
{
    data class Employee(
        var experience: Double? = null,
        var name: String? = null,
        var practice: String? = null,
        var skill: List<String?>? = null
    )
}
package com.example.iaido.userdemo.service

import com.example.iaido.userdemo.model.dto.PersonResponse

interface FuzzySearchService {

    fun fuzzySearchOnFullName(nameToSearch: String): PersonResponse;

}

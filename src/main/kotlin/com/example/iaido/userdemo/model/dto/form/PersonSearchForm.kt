package com.example.iaido.userdemo.model.dto.form

import com.example.iaido.userdemo.model.repo.enum.SearchFields
import com.example.iaido.userdemo.model.repo.enum.SearchType

/**
 * Instructions for the admin/guest search which defaults to FORENAME & AGE with an or search.
 * values set to a default of 0 or  empty string. Relying on front end to set proper values.
 */
class PersonSearchForm(
    var age: Int = 0,
    var forename: String = "",
    var surname: String = "",
    var email: String = "",
    var phone: Long = 0L,
    var searchFields: List<SearchFields> = listOf(SearchFields.FORENAME, SearchFields.AGE),
    var searchType: SearchType = SearchType.OR
) {


}

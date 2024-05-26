package com.example.iaido.userdemo.model.repo

import com.example.iaido.userdemo.model.dto.form.PersonSearchForm
import com.example.iaido.userdemo.model.entity.Person
import com.example.iaido.userdemo.model.repo.enum.SearchFields
import com.example.iaido.userdemo.model.repo.enum.SearchType
import org.springframework.data.jpa.domain.Specification
import java.util.*
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class PersonSpecification(private var personSearchForm: PersonSearchForm) : Specification<Person> {


    private fun stringContainsIgnoringCase(fieldValue: String, fieldName: String): Specification<Person> {
        return Specification<Person> { person, query, builder ->
            if (fieldValue.isBlank()) {
                builder.conjunction();
            }
            builder.like(builder.upper(person.get(fieldName)), "%${fieldValue.lowercase(Locale.getDefault())}%")
        }
    }

    private fun numericFieldEquals(fieldValue: Number, fieldName: String): Specification<Person> {
        return Specification<Person> { person, query, builder ->
            builder.equal(person.get<Int>(fieldName), fieldValue);
        }
    }

    /**
     *  Loop through the specified search fields in personSearchForm, dynamically building up a specification<Person>
     *      that is combined with the SearchType of the personSearchForm to create a predicate.
     *
     */
    override fun toPredicate(
        root: Root<Person>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {

        val joinedSpecification = personSearchForm.searchFields.stream().map { searchField -> getSpecificationForField(searchField) }
                .reduce { combinedSpecification, newSpecification ->
                    if (personSearchForm.searchType == SearchType.AND) {
                        combinedSpecification.and(newSpecification)
                    } else {
                        combinedSpecification.or(newSpecification)
                    }
                }.get();

        return Specification.where(joinedSpecification).toPredicate(root, query, criteriaBuilder);
    }

    private fun getSpecificationForField(searchField: SearchFields): Specification<Person> {
        return when (searchField) {
            SearchFields.AGE ->
                numericFieldEquals(personSearchForm.age, searchField.fieldName)
            SearchFields.FORENAME ->
                stringContainsIgnoringCase(personSearchForm.forename, searchField.fieldName)
            SearchFields.SURNAME ->
                stringContainsIgnoringCase(personSearchForm.surname, searchField.fieldName)
            SearchFields.EMAIL ->
                stringContainsIgnoringCase(personSearchForm.email, searchField.fieldName)
            SearchFields.PHONE ->
                numericFieldEquals(personSearchForm.phone, searchField.fieldName)
        }
    }


}

package com.mithilesh.eventmanagement.specification;

import com.mithilesh.eventmanagement.entity.CovidAffectedArea;
import com.mithilesh.eventmanagement.entity.Events;
import com.mithilesh.eventmanagement.entity.States;
import com.mithilesh.eventmanagement.repository.CovidAffectedAreaRepo;
import com.mithilesh.eventmanagement.repository.StateRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class EventSpecification {

    private final CovidAffectedAreaRepo covidAffectedAreaRepo;
    private final StateRepo stateRepo;

    /**
     * Create specification for the event name
     * <p>
     *     If the name is null or empty, no filtering is applied
     * </p>
     *
     * @param eventName
     * @return
     */
    public Specification<Events> hasEventName(String eventName){
        return (root,query,cb) -> {
            if(eventName == null) {
                return cb.conjunction();
            }

            return cb.like(
                    cb.lower(root.get("eventName")),
                            "%" + eventName.toLowerCase() + "%"
            );
        };
    }

    /**
     * Create Specification for Description
     * <p>
     *     If the descritpion is null or empty no filter is applied
     * </p>
     *
     * @param description
     * @return
     */
    public Specification<Events> hasDescription(String description){
        return (root,query,cb) -> {
            if(description == null)
                return cb.conjunction();

            return cb.like(
                    cb.lower(root.get("description")),
                    "%" + description.toLowerCase() + "%"
            );
        };
    }

    /**
     * Specification for filtering the State
     * <p>
     *     If the state is not found in the DB, filter for whole request can be applied
     * </p>
     *
     * @param stateName
     * @return
     */
    public Specification<Events> hasStateName(String stateName){
        return (root,query,cb) -> {
            if(stateName == null || stateName.isBlank())
                return cb.conjunction() ;

            Optional<States> state = stateRepo.findByStateNameIgnoreCase(stateName);

            if(state.isEmpty())
                return cb.disjunction();

            return cb.equal(
                    root.get("states").get("stateId"),
                    state.get().getStateId()
            );
        };
    }

    /**
     * Spicification for DateRange
     * <ul>
     *     <li>Both start Date</li>
     *     <li>Only the Start Date</li>
     *     <li>Only the End Date</li>
     * </ul>
     *
     * @param startDate starting date of any event
     * @param endDate ending date of any event
     * @return
     */
    public Specification<Events> withinDateRange(LocalDate startDate, LocalDate endDate){
        return (root, query, cb) -> {
            if(startDate != null && endDate != null){
                return cb.between(
                        root.get("eventDate"),startDate,endDate
                );
            }
            else if(startDate != null) {
                return cb.lessThanOrEqualTo(
                        root.get("eventDate"), endDate
                );
            }
            else{
                return cb.greaterThanOrEqualTo(
                        root.get("eventDate"),startDate
                );
            }
        };
    }

    /**
     * Spicification for Search query
     *
     *<p>
     *     filter based on the query, search query related to eventName, Description, Venue
     *</p>
     *
     * @param searchQuery
     * @return
     */
    public Specification<Events> searchQuery(String searchQuery){
        return (root, query, cb) -> {
            if(searchQuery == null || searchQuery.isBlank())
                return cb.conjunction();

            String searchString = "%" + searchQuery + "%";

            return cb.or(
                cb.like(cb.lower(root.get("eventName")),searchString),
                cb.like(cb.lower(root.get("description")),searchString),
                cb.like(cb.lower(root.get("venue")),searchString)
            );
        };
    }

    /**
     * Spicification for registration End date
     * <p>
     *     Filter based on the event end date
     * </p>
     *
     * @param registrationEndDate
     * @return
     */
    public Specification<Events> hasRegistrationEndDate(
            LocalDate registrationEndDate) {

        return (root, query, cb) -> {
            if (registrationEndDate == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("registrationEndDate"),
                    registrationEndDate
            );
        };
    }

    /**
     * Spicification for hideCovid
     * <p>
     *      To hide covid areas and filter the area that is not in covid areas
     * </p>
     *
     * @param hideCovid
     * @return
     */
    public Specification<Events> hideCovid(boolean hideCovid){
        return (root, query, cb) -> {
            if (!hideCovid) {
                return cb.conjunction();
            }

            List<Long> stateList = covidAffectedAreaRepo.findAll()
                    .stream()
                    .map(CovidAffectedArea::getStates)
                    .map(States::getStateId)
                    .toList();

            return cb.not(
                    root.get("states").get("stateId").in(stateList)
            );
        };
    }



}

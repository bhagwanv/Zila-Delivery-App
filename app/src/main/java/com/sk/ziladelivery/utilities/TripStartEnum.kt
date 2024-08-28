package com.sk.ziladelivery.utilities

enum class TripStartEnum(val numericType: Int) {

    ACCEPT(0),
    ACCEPT_ASSIGNMENT_PENDING(1),
    START_TRIP(2),
    START_KM_APPROVAL_PENDING(3),
    INPROGRESS(4),
    SEND_CLOSE_KM_APPROVAL(5),
    CLOSE_KM_APPROVAL_PENDING(6),
    END_TRIP(7),
    ISSUE_IN_TRIP(8),
    NOT_LAST_MILE_APP(9),
    REARRANGE(11);

    companion object {
        infix fun from(value: Int): TripStartEnum? = TripStartEnum.values().firstOrNull {
            it.numericType == value
        }
    }

}
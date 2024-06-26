package com.matrixdeveloper.aivita.model


class AuthBean : BaseBean() {

    var authToken: String= ""
    var userID: String= ""
    var name: String= ""
    var username: String= ""
    var firstName: String= ""
    var lastName: String= ""
    var email: String= ""
    var password: String= ""
    var phone: String= ""
    var gender: String= ""
    var DOB: String= ""
    var profilePhoto: String= ""
    var coverPhoto: String= ""
    var country: String= ""
    var address: String= ""
    var state: String= ""
    var city: String= ""
    var isAllDocumentsUploaded: Boolean = false
    var isAllDocumentsVerified: Boolean = false
    var isPhoneVerified: Boolean = false
}

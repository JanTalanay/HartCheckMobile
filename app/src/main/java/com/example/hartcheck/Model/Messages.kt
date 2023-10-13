package com.example.hartcheck.Model

data class Messages (
    var messagesID: Int,
    var chatID: Int,
    var recieverID: Int,
    var senderID: Int,
    var content: String,
    var dateSent: String
)
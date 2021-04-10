const admin = require('firebase-admin');


exports.sendFriendAlert = async (tokens, body) => {
    let message = {
        data: {
            message: body,
            type: "alert"
        },
        tokens,
    }
    try {
        await admin.messaging().sendMulticast(message);
    } catch (err) {
        console.log("FCM error", err);
    }

}

exports.sendInviteNotification = async (tokens, user) => {
    let body = `You have received invite from ${user.name}: ${user.email}`
    let message = {
        data: {
            message: body,
            type: 'invite',
        },
        tokens,
    }
    try {
        await admin.messaging().sendMulticast(message)
    } catch (err) {
        console.log("FCM error", err);
    }

}
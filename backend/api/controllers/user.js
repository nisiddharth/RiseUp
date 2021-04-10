const { User, Emotion } = require('../../models');
const { sendInviteNotification } = require('../../utils/firebase/notification');

exports.saveDeviceToken = async (req, res) => {
    const { _id, email } = req.auth;
    const { token } = req.body;
    try {
        const user = await User.findOne({ _id });
        if (!user.deviceTokens.includes(token))
            await User.findOneAndUpdate({ _id }, { $push: { deviceTokens: token } });

        return res.json({
            status: 1,
            message: "Device token saved successfully!",
        });

    } catch (err) {
        console.log("Save device token error", err);
        return res.status(500).json({
            status: 0,
            message: "Something went wrong!",
        })
    }
}

exports.removeDeviceToken = async (req, res) => {
    const { _id, email } = req.auth;
    try {
        await User.findOneAndUpdate({ _id }, { deviceTokens: [] });
        return res.json({
            status: 1,
            message: "Device tokens removed successfully!",
        });
    } catch (err) {
        console.log("Save Emotion Error", err);
        return res.status(500).json({
            status: 0,
            message: "Something went wrong!",
        })
    }
}

exports.saveEmotion = async (req, res) => {
    const { _id, email } = req.auth;
    /*
        emotions = [[timestamp, emotion]]
    */
    const { emotions } = req.body;
    try {
        const user = await User.findOne({ _id });

        if (!user.emotion) {
            const emotion = new Emotion({ data: emotions });
            await emotion.save();
            await User.findOneAndUpdate({ _id }, { emotion: emotion._id });
        } else
            await Emotion.findOneAndUpdate({ _id: user.emotion }, { $push: { data: { $each: emotions } } });

        return res.json({
            status: 1,
            message: "Emotions saved successfully!",
        })
    } catch (err) {
        console.log("Save Emotion Error", err);
        return res.status(500).json({
            status: 0,
            message: "Something went wrong!",
        })
    }
}

exports.getEmotions = async (req, res) => {
    const { _id } = req.auth;
    try {
        const user = await User.findOne({ _id });
        if (!user.emotion) {
            return res.status(404).json({
                status: 0,
                message: "Emotion not found!",
            });
        }

        const emotion = await Emotion.findOne({ _id: user.emotion });

        return res.json({
            status: 1,
            data: emotion.data,
        })
    } catch (err) {
        console.log("Get Emotion Error", err);
        return res.status(500).json({
            status: 0,
            message: "Something went wrong!",
        })
    }
}

exports.acceptInvite = async (req, res) => {
    const { friend_id } = req.body;
    const { _id } = req.auth;
    try {
        const user = await User.findOne({ _id });
        if (!user.requests.includes(friend_id)) {
            return res.status(404).json({
                status: 0,
                message: "Friend request not sent!",
            })
        }

        await User.findOneAndUpdate({ _id }, { $push: { friends: friend_id } });
        await User.findOneAndUpdate({ _id: friend_id }, { $push: { friends: _id } });
    } catch (err) {
        console.log("Get Emotion Error", err);
        return res.status(500).json({
            status: 0,
            message: "Something went wrong!",
        })
    }
}

exports.getAllRequests = async (req, res) => {
    const { _id } = req.auth;
    try {
        const user = await User.findOne({ _id });
        const friends = [];
        for (let friendId of user.requests) {
            let friend = await User.findOne({ _id: friendId });
            friends.push(friend.getMeta());
        }

        return res.json({
            status: 1,
            message: "Request successful!",
            data: friends,
        });
    } catch (err) {
        console.log("Get Emotion Error", err);
        return res.status(500).json({
            status: 0,
            message: "Something went wrong!",
        })
    }

}

exports.addFriend = async (req, res) => {
    const { identifier } = req.body;
    const { _id } = req.auth;
    try {
        let isNumber = /^\d+$/.test(identifier);
        let friend;
        if (!isNumber) {
            friend = await User.findOne({ email: identifier });
        } else {
            friend = await User.findOne({ phone: identifier });
        }
        const user = await User.findOne({ _id });

        if (!friend) {
            return res.status(404).json({
                status: 0,
                message: "User not found!",
            });
        }
        if (_id == friend._id) {
            return res.status(422).json({
                status: 0,
                message: "Entered your own cred!",
            })
        }
        if (friend.requests.includes(friend._id))
            return res.status(422).json({
                status: 0,
                message: "Request already sent!",
            });

        if (user.friends.includes(friend._id)) {
            return res.status(422).json({
                status: 0,
                message: "Friend already added!",
            });
        }

        await User.findOneAndUpdate({ _id: friend._id }, { $push: { requests: _id } });
        sendInviteNotification(friend.deviceTokens, user);

        return res.json({
            status: 1,
            message: "Invite sent successfully!",
        })

    } catch (err) {
        console.log("Get Emotion Error", err);
        return res.status(500).json({
            status: 0,
            message: "Something went wrong!",
        })
    }
}

exports.getFriends = async (req, res) => {
    const { _id } = req.auth;
    try {
        const user = await User.findOne({ _id });

        let friends = [];
        for (let friendId of user.friends) {
            const friend = await User.findOne({ _id: friendId });
            friends.push(friend.getMeta());
        }

        return res.json({
            status: 1,
            message: "Friends fetched successfully!",
            data: friends,
        });

    } catch (err) {
        console.log("Get Emotion Error", err);
        return res.status(500).json({
            status: 0,
            message: "Something went wrong!",
        })
    }
}

const { User, Emotion } = require('../../models');

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

exports.addFriend = async (req, res) => {
    const { email, phone } = req.body;
    const { _id } = req.auth;
    try {
        let friend = await User.findOne({ $or: [{ email }, { phone }] });

        if (!friend) {
            return res.status(404).json({
                status: 0,
                message: "User not found!",
            });
        }
        if (_id == friend._id) {
            return res.status(422).json({
                status: 0,
                message: "Entered your own cred!"
            })
        }

        await User.findOneAndUpdate({ _id }, { $push: { friends: friend._id } });
        await User.findOneAndUpdate({ _id: friend._id }, { $push: { friends: _id } });

        return res.json({
            status: 1,
            message: "Friend added successfully!",
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

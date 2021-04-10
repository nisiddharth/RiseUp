const { User, Emotion } = require('../../models');

exports.getFriendEmotions = async (req, res) => {
    const { friend_id } = req.query;
    try {
        const friend = await User.findOne({ _id: friend_id });
        if (!friend) {
            return res.json({
                status: 0,
                message: "Friend doesn't exists!",
            })
        }
        const emotion = await Emotion.findOne({ _id: friend.emotion });
        return res.json({
            status: 1,
            message: "Emotion fetched successfully!",
            data: emotion.data,
        })
    } catch (err) {
        console.log("Error", err);
        return res.status(500).json({
            status: 0,
            message: "Something went wrong!",
        })
    }
}
const { User, Emotion } = require('../../models');

exports.getFriendEmotions = async (req, res) => {
    const { friend_id, prev_day } = req.query;
    try {
        let pastDay = new Date();
        pastDay.setDate(pastDay.getDate() - parseInt(prev_day)-1);
        let presentDay = new Date();
        presentDay.setDate(presentDay.getDate() - parseInt(prev_day));

        const friend = await User.findOne({ _id: friend_id });
        if (!friend) {
            return res.json({
                status: 0,
                message: "Friend doesn't exists!",
            })
        }
        let emotion = await Emotion.findOne({ _id: friend.emotion });
        let data = [];
        let allData = emotion.data.reverse();
        for (let item of allData) {
            console.log("Item", item);
            if (parseInt(item[0]) < pastDay.getTime())
                break;
            if (parseInt(item[0]) <= presentDay.getTime())
                data.push(item);
        }
        return res.json({
            status: 1,
            message: "Emotion fetched successfully!",
            data,
        })
    } catch (err) {
        console.log("Error", err);
        return res.status(500).json({
            status: 0,
            message: "Something went wrong!",
        })
    }
}
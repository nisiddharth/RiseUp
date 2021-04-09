const { User } = require('../../models');

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
const { User } = require('../../models');
const expressJWT = require('express-jwt');
const jwt = require('jsonwebtoken');
const { appConfig } = require('../../config');

exports.signIn = async (req, res) => {
    const { email, password } = req.body;
    try {
        const user = await User.findOne({ email });
        if (!user)
            return res.status(404).json({
                status: 0,
                message: "User doesn't exists",
            });


        const passwordVerification = user.authenticate(password);
        if (!passwordVerification)
            return res.status(401).json({
                status: 0,
                message: "Password is wrong!",
            });

        //Generated the token
        const token = jwt.sign({ _id: user._id, email, }, appConfig.jwtSecret);

        return res.json({
            status: 1,
            message: "SignIn Successful!",
            token,
        });

    } catch (err) {
        console.log("Sign In Error", err);
        return res.status(500).json({
            status: 0,
            message: "Something went wrong!",
        })
    }
}

exports.signUp = async (req, res) => {
    const { body } = req;
    const { email, phone } = body;
    try {

        const userExists = await User.exists({ $or: [{ email }, { phone }] });
        if (userExists)
            return res.status(401).json({
                status: 0,
                message: "User already exists!",
            });

        const user = new User(body);
        await user.save();

        return res.json({
            status: 1,
            message: "User signup successful!",
        });

    } catch (err) {
        console.log('SignUp error', err);
        return res.status(500).json({
            status: 0,
            message: "Something went wrong!", s
        })
    }
}

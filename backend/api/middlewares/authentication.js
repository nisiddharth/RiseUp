const expressJWT = require('express-jwt');


exports.userAuthentication = [
    expressJWT({
        secret: process.env.JWT_SECRET,
        userProperty: "auth",
        algorithms: ['HS256'],
    }),
    (req, res, next) => {
        const { _id, email } = req.auth;
        try {
            next();
        } catch (err) {
            console.log("User authentication error", err);
            return res.status(500).json({
                status: 0,
                message: "Something went wrong!",
            })
        }
    }
]
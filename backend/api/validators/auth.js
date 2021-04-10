const { check, validationResult } = require('express-validator');

exports.signInValidator = [
    check('email').isEmail().exists().withMessage("Invalid Email"),
    check('password').exists().withMessage("Password should exist"),
    (req, res, next) => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            return res.status(422).json({
                status: 0,
                message: "Enter proper fields",
                error: errors.array(),
            });
        }
        next();
    }
]

exports.signUpValidator = [
    check('email').isEmail().exists().withMessage("Email should be valid"),
    check('password').isLength({ min: 8 }).exists().withMessage("Password should be at least 8 characters"),
    check('name').isLength({ min: 2 }).exists().withMessage("Name should be valid!"),
    (req, res, next) => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            return res.status(422).json({
                status: 0,
                message: "Enter proper fields",
                error: errors.array(),
            });
        }
        next();
    }
]
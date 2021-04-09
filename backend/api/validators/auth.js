const { check, validationResult } = require('express-validator');

exports.signInValidator = [
    check('email').isEmail().exists(),
    check('password').exists(),
    (req, res, next) => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            return res.status(422).json({
                status: 0,
                error: errors.array(),
            });
        }
        next();
    }
]

exports.signUpValidator = [
    check('email').isEmail().exists(),
    check('password').isLength({ min: 8 }).exists(),
    check('name').isLength({ min: 2 }).exists(),
    (req, res, next) => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            return res.status(422).json({
                status: 0,
                error: errors.array(),
            });
        }
        next();
    }
]
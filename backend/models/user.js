const mongoose = require('mongoose');
const crypto = require('crypto');
const uuidv1 = require('uuid').v1;

const UserSchema = new mongoose.Schema({
    name: {
        type: String,
        required: true,
    },
    email: {
        type: String,
        required: true,
        unique: true,
    },
    phone: {
        type: String,
        required: true,
        unique: true,
    },
    deviceTokens: [
        {
            type: String,
        }
    ],
    emotion: {
        type: mongoose.Types.ObjectId,
        ref: 'emotions'
    },
    friends: [
        {
            type: mongoose.Types.ObjectId,
            ref: 'user',
        }
    ],
    requests: [
        {
            type: mongoose.Types.ObjectId,
            ref: 'user',
        }
    ],
    encryptedPassword: String,
    salt: String,
}, {
    timestamps: true,
    toObject: {
        virtuals: true,
    },
    toJSON: {
        virtuals: true,
    }
});


UserSchema.virtual('password').set(function (password) {
    this.salt = uuidv1();
    this.encryptedPassword = this.securePassword(password);
});


UserSchema.methods = {
    authenticate: function (plainPassword) {
        return this.securePassword(plainPassword) === this.encryptedPassword
    },

    securePassword: function (plainPassword) {
        if (!plainPassword) return "";
        try {
            return crypto.createHmac('sha256', this.salt)
                .update(plainPassword)
                .digest('hex');
        } catch (err) {
            return "";
        }
    },
    getMeta: function () {
        return {
            name: this.name,
            phone: this.phone,
            email: this.email,
            _id: this._id,
        }
    }
}

module.exports = mongoose.model('user', UserSchema);
const mongoose = require('mongoose');

const EmotionSchema = new mongoose.Schema({
    data: [
        {
            type: Array,
        }
    ]
}, {
    timestamps: true,
    toObject: {
        virtuals: true,
    },
    toJSON: {
        virtuals: true,
    }
});

module.exports = mongoose.model('emotions', EmotionSchema);
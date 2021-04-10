const { google } = require('googleapis');

let keyword = {
    anger: "anger management, anger control motivation, avoid anger",
    neutral: "mental wellbeing",
    happiness: "mental wellbeing",
    sadness: "happiness motivation",
    surprise: "mental wellbeing",
}

exports.getVideos = async (req, res) => {
    const { emotion } = req.params;
    try {
        const response = await google.youtube('v3').search.list({
            key: "AIzaSyA-uNkzIl2BUC3DOX676G4M0Itk8Xv17YI",
            q: keyword[emotion] || "mental wellbeing",
            part: 'snippet',
        });

        let { data } = response;

        let videos = [];

        for (let video of data.items) {
            videos.push({
                title: video.snippet.title,
                description: video.snippet.description,
                thumbnails: [video.snippet.thumbnails.default.url, video.snippet.thumbnails.medium.url, video.snippet.thumbnails.high.url],
                channel: video.snippet.channelTitle,
                videoId: video.id.videoId
            })
        }
        return res.json({
            status: 1,
            data: videos
        })
    } catch (err) {
        console.log("Error", err);

    }

}
const { google } = require('googleapis');

let keyword = {
    anger: "anger management, anger control motivation, avoid anger",
    sad: "",
    happy: "",
    Disgust: "disgust feeling, disgust emotion",
    Fear: "fear management, face fear, overcome fear",
    Sad: "happiness motivation",

}

exports.getVideos = async (req, res) => {
    let emotion = "anger";
    console.log('Enter');
    try {
        const response = await google.youtube('v3').search.list({
            key: "AIzaSyA-uNkzIl2BUC3DOX676G4M0Itk8Xv17YI",
            q: keyword[emotion] || "emotions",
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
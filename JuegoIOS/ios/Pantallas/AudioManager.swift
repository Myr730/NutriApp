//
//  AudioManager.swift
//  Pantallas
//
//  Created by Karol Lozano González on 18/11/25.
//

import AVFoundation

class AudioManager {
    static let shared = AudioManager()
    private var backgroundMusicPlayer: AVAudioPlayer?
    private var buttonPlayer: AVAudioPlayer?

    private init() {}

    func playBackgroundMusic() {
        // If already playing, do NOT restart
        if backgroundMusicPlayer?.isPlaying == true {
            return
        }

        guard let url = Bundle.main.url(forResource: "musicamain", withExtension: "mp3") else {
            return
        }

        do {
            backgroundMusicPlayer = try AVAudioPlayer(contentsOf: url)
            backgroundMusicPlayer?.numberOfLoops = -1
            backgroundMusicPlayer?.volume = 0.5
            backgroundMusicPlayer?.prepareToPlay()
            backgroundMusicPlayer?.play()
        } catch {
        }
    }

    func muteBackgroundMusic() {
        backgroundMusicPlayer?.setVolume(0, fadeDuration: 0.4)
    }

    func unmuteBackgroundMusic() {
        backgroundMusicPlayer?.setVolume(0.5, fadeDuration: 0.4)
    }

    func playButtonSound() {
        guard let url = Bundle.main.url(forResource: "Botones", withExtension: "mp3") else {
            return
        }

        do {
            // Recreate player each time so it can overlap properly
            buttonPlayer = try AVAudioPlayer(contentsOf: url)
            buttonPlayer?.volume = 1.0
            buttonPlayer?.prepareToPlay()
            buttonPlayer?.play()
        } catch {
        }
    }

}


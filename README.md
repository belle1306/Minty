# Inspiration
Inspired by the thousands of dollars spent on trading cards when I was a kid, I created an app where users can create mint their own digital trading cards. 
Other apps built on blockchain don't have the customizable 3D card which is my favorite aspect of this app. Anyone can easily mint their own cards without a barrier for entry (learning 3d modeling, texturing, etc).

# How I built it
- Minty is built on Android with Java. The NFTs are stored on the symbol blockchain. The card preview and card textures are stored offchain onto IPFS.

- The user uploads the textures to IPFS, and uses the hashes to be stored as metadata onto the NFTs

- The holographic texture (the glossy background of the card) is a feature, you can also use it without it, but for the demo I kept only that version because I thought it would look cooler.

# Challenges I ran into
I understand Symbol is quite new so I don't blame them, but the Java/Android SDK for symbol need work. I ran into multiple bugs that took quite a bit of time to fix by using workarounds.... I ended up having to port everything to the Javascript SDK to work because I couldn't get it to work natively on Android.
The documentation can also be improved. However, the symbol desktop wallet was quite nice to use.

# What's next for Minty

I might consider creating a few more canvas sizes for the 3d model so people can easily add artwork onto them, but for now I plan on keeping the app to simply creating only digital trading cards.
I do want to take it into production if I can get enough support for my app because I really like the idea.

/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{js,jsx,ts,tsx}",],
  darkMode: 'selector',
  theme: {
    extend: {
      backgroundImage: {
        'doctor-background': "url('/src/assets/DoctorAnime.png')"
      }
    },
  },
  plugins: [],
}


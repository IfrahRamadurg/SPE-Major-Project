FROM node:alpine
WORKDIR /jobPortalApp
COPY ./ /jobPortalApp/
RUN npm install --legacy-peer-deps
COPY . /jobPortalApp
CMD ["npm","start"]
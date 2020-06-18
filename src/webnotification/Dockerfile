FROM node:8.9-alpine

# Create app directory
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

# Install app dependencies
RUN apk update && apk upgrade && apk add --no-cache git

COPY package.json /usr/src/app/package.json
RUN npm install --no-optional
#RUN npm ci --only=production

COPY . /usr/src/app/
#RUN npm run build

ENV HOST 0.0.0.0
EXPOSE 3001

# start command
CMD [ "node", "app.js" ]
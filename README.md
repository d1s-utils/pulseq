[![pulseq](https://raw.githubusercontent.com/d1snin-dev/pulseq/master/pulseq/src/main/resources/static/pulseq.png)](https://pq.d1s.uno/)
[![CircleCI](https://circleci.com/gh/d1snin-dev/pulseq/tree/master.svg?style=shield)](https://circleci.com/gh/d1snin-dev/pulseq/tree/master)
[![wakatime](https://wakatime.com/badge/user/e4446807-0aa6-4ba9-92ea-2a7632bc44c9/project/46213356-3912-4014-96a6-4aa34d768a68.svg)](https://github.com/d1snin-dev/pulseq)
[![CodeFactor](https://www.codefactor.io/repository/github/d1snin-dev/pulseq/badge)](https://www.codefactor.io/repository/github/d1snin-dev/pulseq)
[![pulseq-last-beat](https://pq.d1s.uno/api/badge/last-beat)](https://pq.d1s.uno/)
[![pulseq-last-beat-time](https://pq.d1s.uno/api/badge/last-beat-time)](https://pq.d1s.uno/)
[![License](https://img.shields.io/badge/License-BSD%203--Clause-blue.svg)](https://opensource.org/licenses/BSD-3-Clause)
[![total-lines](https://img.shields.io/tokei/lines/github/d1snin-dev/pulseq?color=orange)](https://github.com/d1snin-dev/pulseq)

> Pulseq is inspired by [`technically-functional/heartbeat`](https://github.com/technically-functional/heartbeat), which is licensed under the [ISC license](https://github.com/technically-functional/heartbeat/blob/master/LICENSE.md).

The main idea of pulseq is to provide statistics on your device usage and notify you if you haven't been online for too long.

#### Try pulseq in action right now!
```shell
git clone https://github.com/d1snin-dev/pulseq.git &&\
  cd ./pulseq &&\
  ./gradlew pulseq:bootBuildImage &&\
  docker-compose up -d
```

[***Read the docs***](https://github.com/d1snin-dev/pulseq/wiki)

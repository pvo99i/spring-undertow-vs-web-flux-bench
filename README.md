# Demo applications for benchmarking of spring-web-flux vs spring-undertow + mvc

## Project structure

This project consists of those modules:

- core (common repository and service code)
- async (spring-web-flux application)
- sync (spring-boot-undertow application)

## Prerequisites
- docker
- docker-compose
- wrk

## Benchmarks

My laptop has following configuration:
- Linux localhost.localdomain 4.9.13-200.fc25.x86_64 #1 SMP Mon Feb 27 16:48:42 UTC 2017 x86_64 x86_64 x86_64 GNU/Linux   
- Intel(R) Core(TM) i7-6700HQ CPU @ 2.60GHz
- 16G RAM
- 128G SSD
- java version "1.8.0_102"

### Preparation
- Start docker: ```./start-docker.sh```
- Start application: ```./gradlew clean :async:bootRun``` or ```./gradlew clean :sync:bootRun```
- Populate database: ```curl http://localhost:8080/populate```

### spring-web-flux

Benchmarking with spring's default cache:
```bash
 wrk -H "Accept:application/stream+json"  -L -t8 -c1024 --timeout 10  http://localhost:8080/person/6
Running 10s test @ http://localhost:8080/person/18006
  8 threads and 1024 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    11.22ms    5.84ms 247.45ms   81.68%
    Req/Sec    10.57k     1.81k   15.65k    78.28%
  Latency Distribution
     50%    9.69ms
     75%   12.72ms
     90%   18.25ms
     99%   32.75ms
  842029 requests in 10.06s, 256.97MB read
  Socket errors: connect 11, read 0, write 0, timeout 0
Requests/sec:  83727.65
Transfer/sec:     25.55MB
```

Benchmarking without cache:
```bash
wrk -H "Accept:application/stream+json"  -L -t8 -c1024 --timeout 10  http://localhost:8080/noncached/6
Running 10s test @ http://localhost:8080/noncached/6
  8 threads and 1024 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   122.23ms   30.02ms 552.52ms   79.26%
    Req/Sec     0.94k   284.22     1.60k    76.12%
  Latency Distribution
     50%  119.34ms
     75%  130.15ms
     90%  152.30ms
     99%  200.20ms
  75059 requests in 10.04s, 22.62MB read
  Socket errors: connect 11, read 0, write 0, timeout 0
Requests/sec:   7472.76
Transfer/sec:      2.25MB
```
### spring-web-undertow
Benchmarking using spring's default cache:
```bash
wrk -H "Accept:application/stream+json"  -L -t8 -c1024 --timeout 10  http://localhost:8080/person/6
Running 10s test @ http://localhost:8080/person/6
  8 threads and 1024 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    19.79ms    4.58ms 224.69ms   79.91%
    Req/Sec     6.24k     0.94k    9.18k    74.59%
  Latency Distribution
     50%   18.99ms
     75%   21.49ms
     90%   24.89ms
     99%   36.67ms
  500756 requests in 10.10s, 183.38MB read
  Socket errors: connect 11, read 0, write 0, timeout 0
Requests/sec:  49602.48
Transfer/sec:     18.17MB
```

Benchmarking without cache:
```bash
wrk -H "Accept:application/stream+json"  -L -t8 -c1024 --timeout 10  http://localhost:8080/noncached/6
Running 10s test @ http://localhost:8080/noncached/6
  8 threads and 1024 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   200.85ms   80.30ms 604.44ms   85.62%
    Req/Sec   625.02    328.88     1.29k    63.55%
  Latency Distribution
     50%  167.55ms
     75%  214.36ms
     90%  344.15ms
     99%  446.55ms
  47762 requests in 10.03s, 17.49MB read
  Socket errors: connect 11, read 0, write 0, timeout 0
Requests/sec:   4759.99
Transfer/sec:      1.74MB

```







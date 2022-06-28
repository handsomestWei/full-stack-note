# ffmpeg-desktop-live
使用**nginx**+**rtmp**+**ffmpeg**实现桌面直播

## 流媒体服务器搭建
### docker run
docker镜像基于添加了rtmp模块的nginx，和ffmpeg
```
docker pull alfg/nginx-rtmp
docker run -d -p 1935:1935 -p 8080:80 --name=nginx-rtmp alfg/nginx-rtmp
```

### rtmp模块说明
进入容器内部查看
```
docker ps | grep alfg/nginx-rtmp
docker exec -it [docker镜像id] /bin/sh
cat /etc/nginx/nginx.conf
```

<details>
<summary>nginx rtmp模块配置</summary>
<pre><code>rtmp {
    server {
        listen 1935;
        chunk_size 4000;

        application stream {
            live on;

            exec ffmpeg -i rtmp://localhost:1935/stream/$name
              -c:a libfdk_aac -b:a 128k -c:v libx264 -b:v 2500k -f flv -g 30 -r 30 -s 1280x720 -preset superfast -profile:v baseline rtmp://localhost:1935/hls/$name_720p2628kbs
              -c:a libfdk_aac -b:a 128k -c:v libx264 -b:v 1000k -f flv -g 30 -r 30 -s 854x480 -preset superfast -profile:v baseline rtmp://localhost:1935/hls/$name_480p1128kbs
              -c:a libfdk_aac -b:a 128k -c:v libx264 -b:v 750k -f flv -g 30 -r 30 -s 640x360 -preset superfast -profile:v baseline rtmp://localhost:1935/hls/$name_360p878kbs
              -c:a libfdk_aac -b:a 128k -c:v libx264 -b:v 400k -f flv -g 30 -r 30 -s 426x240 -preset superfast -profile:v baseline rtmp://localhost:1935/hls/$name_240p528kbs
              -c:a libfdk_aac -b:a 64k -c:v libx264 -b:v 200k -f flv -g 15 -r 15 -s 426x240 -preset superfast -profile:v baseline rtmp://localhost:1935/hls/$name_240p264kbs;
        }

        application hls {
            live on;
            hls on;
            hls_fragment_naming system;
            hls_fragment 5;
            hls_playlist_length 10;
            hls_path /opt/data/hls;
            hls_nested on;

            hls_variant _720p2628kbs BANDWIDTH=2628000,RESOLUTION=1280x720;
            hls_variant _480p1128kbs BANDWIDTH=1128000,RESOLUTION=854x480;
            hls_variant _360p878kbs BANDWIDTH=878000,RESOLUTION=640x360;
            hls_variant _240p528kbs BANDWIDTH=528000,RESOLUTION=426x240;
            hls_variant _240p264kbs BANDWIDTH=264000,RESOLUTION=426x240;
        }
    }
}
</code></pre>
</details>

## 推流
使用ffmpeg录屏桌面，视频流推送到搭建好的流媒体服务器
### windows环境
```
ffmpeg -f gdigrab -r 25 -s 1920*780 -i desktop -f flv rtmp://ip:1935/hls/desktop.1920.flv
```
### linux环境
```
ffmpeg -f x11grab -r 25 -s 1920*780 -qscale 0.01 -i :0.0 -f flv rtmp://ip:1935/hls/desktop.1920.flv
```

## 拉流
### 播放器播放
[vlc播放器下载](https://www.videolan.org/vlc/)
```
打开vlc播放器，选择“打开-》网络串流”并输入推流的url，即可从流媒体服务器拉取视频流并播放
```
### vue展示
```
使用vue-video-player组件，src为推流的url
```

## 参考
[nginx+rtmp模块docker镜像](https://registry.hub.docker.com/r/alfg/nginx-rtmp)   
[ffmpeg官网](http://ffmpeg.org/)   
[srs](http://www.ossrs.net/srs.release/releases/)

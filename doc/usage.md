## docker部署zookeeper
- 下载zookeeper镜像
```shell
docker pull zookeeper
```
- 运行zookeeper容器
```shell
docker run -d -e TZ="Asia/Shanghai" -p 2181:2181 -v $PWD/data:/data --name zookeeper --restart always zookeeper
```
- 进入zookeeper容器
```shell
docker run -it --rm --link zookeeper:zookeeper zookeeper zkCli.sh -server zookeeper
```
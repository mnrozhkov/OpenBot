

## Install & Setup virtual environment
Build DEV environment (Python Virtual Environment)
Create and activate virtual environment for local development

```bash 
python3 -m venv .venv
echo "export PYTHONPATH=$PWD" >> .venv/bin/activate
source .venv/bin/activate
git clone https://github.com/ultralytics/yolov5.git
pip install --upgrade pip setuptools wheel
pip install -r yolov5/requirements.txt
pip install -r requirements.txt
``` 


## Install & Setup Docker environment
<!-- Reference: [YoloV5 Docker Quickstart](https://github.com/ultralytics/yolov5/wiki/Docker-Quickstart) 

GPU-based: 

```bash 
setup.sh --env [ENVIRONMENT]  # virtual-env, docker-gpu, docker-cpu, all
```

```bash
activate.sh --env [ENVIRONMENT]  # virtual-env, docker-gpu, docker-cpu, all 
``` -->

Test installation
```bash 
python yolov5/val.py --weights yolov5s.pt
```

Run docker container in interactve mode:
```
export YOLOV5_IMAGE="ultralytics/yolov5:latest-cpu"
docker run -it --ipc=host \
    -v "$(pwd)"/datasets:/usr/src/datasets \
    -v "$(pwd)"/runs:/usr/src/app/runs \
    $YOLOV5_IMAGE
```

Run python script inside docker container and shut down on completion:
```
docker run -it --ipc=host \
    -v "$(pwd)"/datasets:/usr/src/datasets \
    -v "$(pwd)"/runs:/usr/src/app/runs \
    $YOLOV5_IMAGE \
    python3 val.py --weights yolov5s.pt --exist-ok
```
inside interactive docker container session:  
    `python3 val.py --weights yolov5s.pt` 

## Build custom Docker image

```bash
./build_image.sh
```

## Create `.env`

```bash
touch .env
```

Add to `.env` custom image name:

`.env`
```dotenv
YOLOV5_IMAGE=openbot-vision-object-detection:latest
```


## Run DVC pipeline

```bash
./run_pipeline.sh
```
source env_placeholder.sh

mkdir ~/.aws/
echo "[default]" > ~/.aws/config
echo "  region=ru-central1" >> ~/.aws/config
echo "[default]" > ~/.aws/credentials
echo "  aws_access_key_id = $AWS_ACCESS_KEY" >> ~/.aws/credentials
echo "  aws_secret_access_key = $AWS_SECRET_KEY" >> ~/.aws/credentials

mkdir -p ~/.postgresql && \
wget "https://storage.yandexcloud.net/cloud-certs/CA.pem" \
    --output-document ~/.postgresql/root.crt && \
chmod 0600 ~/.postgresql/root.crt

sudo apt update && sudo apt install --yes default-jdk maven

./gradlew bootRun

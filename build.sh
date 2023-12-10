source env_placeholder.sh

mkdir ~/.aws/
echo "[default]" > ~/.aws/config
echo "  region=ru-central1" >> ~/.aws/config
echo "[default]" > ~/.aws/credentials
echo "  aws_access_key_id = $AWS_ACCESS_KEY" >> ~/.aws/credentials
echo "  aws_secret_access_key = $AWS_SECRET_KEY" >> ~/.aws/credentials

./gradlew bootRun

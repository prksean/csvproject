echo "프로젝트 packaging 시작"

mvn clean install

echo "jar 생성 완료"
echo "8090 포트 사용중 PID 확인"

USING_PID=$(lsof -t -i:8090)

echo "8090 포트 사용중 PID: $USING_PID"

if [ -z $USING_PID ]; then
	echo "8090 포트 사용중 PID 없음"
else
	echo "$USING_PID 프로세스 죽이기"
	kill -9 $USING_PID
fi

echo "10초 후 Deploy 시작"
sleep 10
echo "Deploy 시작"
nohup java -Dfile.encoding=UTF-8 -jar target/*.jar &

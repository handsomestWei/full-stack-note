set path=%path%;D:\protoc-3.14.0-win64\bin
protoc --version
protoc -I D:\full-stack-note\protobuf-demo\src\main\resources\proto --java_out=D:\full-stack-note\protobuf-demo\src\main\java Order.proto
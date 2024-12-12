# aws-s3-curd
#Steps to run this project:
Create a private S3 bucket
Go to AWS IAM and create a permission:
{
"Version": "2012-10-17",
"Statement": [
{
"Effect": "Allow",
"Action": [
"s3:PutObject",
"s3:GetObject",
"s3:ListBucket"
],
"Resource": [
"arn:aws:s3:::your-bucket-name",
"arn:aws:s3:::your-bucket-name/*"
]
}
]
}

assign this permission to a role or a user. In local assign this to a user and create access key and secret key and replace below things in application.properties.
aws.s3.bucket-name=xxxxxxxxx
aws.s3.region=xxxxxxxxx

aws.access.key=xxxxxxxxx
aws.secret.key=xxxxxxxxx

Test from postman we have 3 apis: generate pre-signed url, download pre-signed url, delete pre-signed url.
Generate pre-signed url by posting fileName and content type using POST api.localhost:8080/api/s3/upload-url

Take the pre-signed url from step1 response and use PUT method to upload actual file to S3 directly.
Select request body as binary.

Then using the key received in step1 can generate the pre-signed url and download.localhost:8080/api/s3/download-url?key=a0921596f930eb8_screenshot2.png

Can delete the file using delete endpoint.localhost:8080/api/s3/delete?objectKey=a0921596f930eb8_screenshot2.png
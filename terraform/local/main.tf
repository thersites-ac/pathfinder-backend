provider "aws" {
  access_key = "foo"
  region = "us-east-1"
  secret_key = "bar"
  skip_credentials_validation = true
  skip_requesting_account_id = true
  skip_metadata_api_check = true

  endpoints {
    lambda = "http://localhost:4566"
    ec2 = "http://localhost:4566"
    route53 = "http://localhost:4566"
    cloudwatch = "http://localhost:4566"
    cloudwatchlogs = "http://localhost:4566"
    iam = "http://localhost:4566"
    apigateway = "http://localhost:4566"
  }
}

module "lambda_endpoint" {
  source = "../modules/endpoint"
  stage_name = var.stage_name
  jarfile = "../../target/tomblywombly.jar"
}

output "local_endpoint" {
  value = "localhost:4566/restapis/${module.lambda_endpoint.rest_api_id}/${var.stage_name}/_user_request_"
}
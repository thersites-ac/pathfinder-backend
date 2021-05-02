provider "aws" {
  access_key = "foo"
  region = "us-east-1"
  secret_key = "bar"
  skip_credentials_validation = true
  skip_requesting_account_id = true
  skip_metadata_api_check = true

  endpoints {
    lambda = var.localstack
    ec2 = var.localstack
    route53 = var.localstack
    cloudwatch = var.localstack
    cloudwatchlogs = var.localstack
    iam = var.localstack
    apigateway = var.localstack
  }
}

module "lambda_endpoint" {
  source = "../modules/endpoint"
  stage_name = var.stage_name
  jarfile = "../../target/tomblywombly.jar"
}


output "invoke_url" {
  value = "http://localhost:4566/restapis/${module.lambda_endpoint.rest_api_id}/${var.stage_name}/_user_request_"
}

output "rest_api_id" {
  value = module.lambda_endpoint.rest_api_id
}

output "resource_id" {
  value = module.lambda_endpoint.resource_id
}

output "integration_uri" {
  value = module.lambda_endpoint.integration_uri
}


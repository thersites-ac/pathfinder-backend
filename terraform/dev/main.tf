provider "aws" {
  region = "us-east-2"
}

module "lambda_endpoint" {
  source = "../modules/endpoint"
  stage_name = var.stage_name
  jarfile = "../../target/tomblywombly.jar"
}

output "base_url" {
  value = module.lambda_endpoint.base_url
}

output "rest_api_id" {
  value = module.lambda_endpoint.rest_api_id
}

output "resource_id" {
  value = module.lambda_endpoint.resource_id
}

output "http_method" {
  value = module.lambda_endpoint.http_method
}

output "integration_type" {
  value = module.lambda_endpoint.integration_type
}

output "integration_http_method" {
  value = module.lambda_endpoint.integration_http_method
}

output "uri" {
  value = module.lambda_endpoint.uri
}


provider "aws" {
  region = "us-east-2"
}

module "lambda_endpoint" {
  source = "../modules/endpoint"
  stage_name = var.stage_name
  jarfile = "../../target/tomblywombly.jar"
  DB_USER = var.DB_USER
  DB_PASSWORD = var.DB_PASSWORD
  DB_URL = var.DB_URL
}

output "invoke_url" {
  value = module.lambda_endpoint.invoke_url
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


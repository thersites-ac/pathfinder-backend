output "base_url" {
  value = aws_api_gateway_deployment.deployment.invoke_url
}

output "rest_api_id" {
  value = aws_api_gateway_integration.lambda.rest_api_id
}

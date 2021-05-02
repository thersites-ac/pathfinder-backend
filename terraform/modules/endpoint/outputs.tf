output "invoke_url" {
  value = aws_api_gateway_deployment.deployment.invoke_url
}

output "rest_api_id" {
  value = aws_api_gateway_integration.lambda.rest_api_id
}

output "resource_id" {
  value = aws_api_gateway_resource.proxy.id
}

output "integration_uri" {
  value = aws_api_gateway_integration.lambda.uri
}


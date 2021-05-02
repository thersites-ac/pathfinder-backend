output "base_url" {
  value = aws_api_gateway_deployment.deployment.invoke_url
}

output "rest_api_id" {
  value = aws_api_gateway_integration.lambda.rest_api_id
}

output "resource_id" {
  value = aws_api_gateway_resource.proxy.id
}

output "http_method" {
  value = aws_api_gateway_method.proxy.http_method
}

output "integration_type" {
  value = aws_api_gateway_integration.lambda.type
}

output "integration_http_method" {
  value = aws_api_gateway_integration.lambda.integration_http_method
}

output "uri" {
  value = aws_api_gateway_integration.lambda.uri
}


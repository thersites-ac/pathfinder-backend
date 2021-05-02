resource "aws_iam_role" "pathfinder_backend_role" {
  name = var.name
  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "lambda.amazonaws.com"
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
EOF
}

resource "aws_lambda_function" "pathfinder_backend" {
  filename = var.jarfile
  function_name = var.name
  handler = "cogbog.App::handleRequest"
  role = aws_iam_role.pathfinder_backend_role.arn
  runtime = "java8"
  source_code_hash = filebase64sha256(var.jarfile)
  depends_on = [
    aws_cloudwatch_log_group.pathfinder_backend_logs,
    aws_iam_role_policy_attachment.lambda_logging_attachment
  ]
  memory_size = 512
  timeout = 5
  environment {
    variables = {
      DB_PASSWORD = var.DB_PASSWORD
      DB_URL = var.DB_URL
      DB_USER = var.DB_USER
    }
  }
}

resource "aws_iam_policy" "lambda_logging" {
  name = var.name
  path = "/"
  description = "IAM policy to allow Pathfinder application logging"
  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": [
        "logs:CreateLogGroup",
        "logs:CreateLogStream",
        "logs:PutLogEvents"
      ],
      "Resource": "arn:aws:logs:*:*:*",
      "Effect": "Allow"
    }
  ]
}
EOF
}

resource "aws_iam_role_policy_attachment" "lambda_logging_attachment" {
  policy_arn = aws_iam_policy.lambda_logging.arn
  role = aws_iam_role.pathfinder_backend_role.name
}

resource "aws_cloudwatch_log_group" "pathfinder_backend_logs" {
  name = "/aws/lambda/${var.name}"
  retention_in_days = 14
}

resource "aws_lambda_permission" "apigw" {
  statement_id = "AllowApiGatewayInvoke"
  action = "lambda:InvokeFunction"
  function_name = aws_lambda_function.pathfinder_backend.function_name
  principal = "apigateway.amazonaws.com"
  source_arn = "${aws_api_gateway_rest_api.pathfinder_backend_api.execution_arn}/*/*"
}

resource "aws_api_gateway_rest_api" "pathfinder_backend_api" {
  name = var.name
  description = "Data persistence layer for Pathfinder profile management app"
}

resource "aws_api_gateway_resource" "proxy" {
  rest_api_id = aws_api_gateway_rest_api.pathfinder_backend_api.id
  parent_id = aws_api_gateway_rest_api.pathfinder_backend_api.root_resource_id
  path_part = "{proxy+}"
}

resource "aws_api_gateway_method" "proxy" {
  rest_api_id = aws_api_gateway_rest_api.pathfinder_backend_api.id
  resource_id = aws_api_gateway_resource.proxy.id
  http_method = "ANY"
  authorization = "NONE"
  request_parameters = {
    "method.request.path.proxy" = true
  }
}

resource "aws_api_gateway_method_response" "proxy_response" {
  http_method = aws_api_gateway_method.proxy.http_method
  resource_id = aws_api_gateway_resource.proxy.id
  rest_api_id = aws_api_gateway_rest_api.pathfinder_backend_api.id
  status_code = "200"
  response_models = {
    "application/json" = "Empty"
  }
}

resource "aws_api_gateway_integration" "lambda" {
  rest_api_id = aws_api_gateway_rest_api.pathfinder_backend_api.id
  resource_id = aws_api_gateway_method.proxy.resource_id
  http_method = aws_api_gateway_method.proxy.http_method

  cache_key_parameters = [
    "method.request.path.proxy"
  ]

  content_handling = "CONVERT_TO_TEXT"

  integration_http_method = "POST"
  type = "AWS_PROXY"
  uri = aws_lambda_function.pathfinder_backend.invoke_arn
  depends_on = [
    aws_lambda_function.pathfinder_backend
  ]
}

resource "aws_api_gateway_deployment" "deployment" {
  depends_on = [
    aws_api_gateway_integration.lambda
  ]
  rest_api_id = aws_api_gateway_rest_api.pathfinder_backend_api.id
  stage_name = var.stage_name
}



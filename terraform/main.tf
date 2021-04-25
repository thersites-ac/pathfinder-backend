resource "aws_iam_role" "pathfinder_backend_role" {
  name = "pathfinder-backend-role"
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
    },
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

variable "jarfile" {
  default = "../target/tomblywombly.jar"
}

variable "function_name" {
  default = "pathfinder-backend"
}

resource "aws_lambda_function" "pathfinder_backend" {
  filename = var.jarfile
  function_name = var.function_name
  handler = "cogbog.App:handleRequest"
  role = aws_iam_role.pathfinder_backend_role.arn
  runtime = "java8"
  source_code_hash = filebase64sha256(var.jarfile)
  depends_on = [
    aws_cloudwatch_log_group.pathfinder_backend_logs
  ]
}

resource "aws_cloudwatch_log_group" "pathfinder_backend_logs" {
  name = "/aws/lambda/${var.function_name}"
  retention_in_days = 14
}
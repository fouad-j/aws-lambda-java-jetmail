data "aws_secretsmanager_secret_version" "secrets" {
  secret_id = var.secrets_manager_arn
}

resource "aws_lambda_function" "contact_lambda_function" {
  filename      = "11-lambda-notification/mailjet_notification_lambda.zip"
  runtime       = "java11"
  function_name = "${var.environment}-mailjet_notification"
  handler       = "com.jfouad.EntryPoint"
  timeout       = 60
  memory_size   = 256

  role       = "${aws_iam_role.notification_lambda_role.arn}"
  depends_on = [aws_cloudwatch_log_group.log_mailjet_notification]

  environment {
    variables = {
      MAILJET_APIKEY_PUBLIC  = jsondecode(data.aws_secretsmanager_secret_version.secrets.secret_string)["MAILJET_APIKEY_PUBLIC"]
      MAILJET_APIKEY_PRIVATE = jsondecode(data.aws_secretsmanager_secret_version.secrets.secret_string)["MAILJET_APIKEY_PRIVATE"]
      PROJECT_CONTACT_MAIL   = jsondecode(data.aws_secretsmanager_secret_version.secrets.secret_string)["PROJECT_CONTACT_MAIL"]
    }
  }

  tags = var.tags
}

resource "aws_iam_role" "notification_lambda_role" {
  name               = "iam-role-lambda-access-${var.environment}"
  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "",
      "Effect": "Allow",
      "Principal": {
        "Service": "lambda.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
EOF

  tags = var.tags
}

resource "aws_cloudwatch_log_group" "log_mailjet_notification" {
  name              = "${var.environment}-mailjet_notification"
  retention_in_days = 5

  tags = var.tags
}


resource "aws_iam_policy" "lambda_logging" {
  name        = "lambda_logging"
  path        = "/"
  description = "IAM policy for logging from a lambda"

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

resource "aws_iam_role_policy_attachment" "lambda_logs" {
  role       = aws_iam_role.notification_lambda_role.name
  policy_arn = aws_iam_policy.lambda_logging.arn
}

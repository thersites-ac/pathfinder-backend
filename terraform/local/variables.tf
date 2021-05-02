variable "stage_name" {
  type = string
  description = "Stage name for lambda endpoint module"
  default = "local"
}

variable "localstack" {
  type = string
  description = "URI of localstack server"
  default = "http://localhost:4566"
}
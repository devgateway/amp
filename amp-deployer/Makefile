
## Avoid conflicts with files of the same name
## @see: http://www.gnu.org/savannah-checkouts/gnu/make/manual/html_node/Phony-Targets.html
.PHONY: dummy bootstrap provision


## Set environment to local if it has not been provided.
ENVIRONMENT?=local

## Provision the current host if one has not been provided.
HOSTS?=localhost


## The default target, if no target argument is provided to `make`.
## Update an environment with the latest changes from code.
dummy:
	@echo 'Here is a list of available commands:'
	@grep -iE "^[a-z_-]+:" $(lastword $(MAKEFILE_LIST)) | cut -d: -f1 | grep -v dummy | xargs -i{} echo "  $$ make {}"


## Bootstrap Ansible on a local EL7 machine.
bootstrap-el7:
	@echo 'Bootstrapping Ansible:'
	@echo 'Installing the EPEL repository.'
	@rpm --quiet --force -Uh https://dl.fedoraproject.org/pub/epel/epel-release-latest-7.noarch.rpm
	@gpg --import /etc/pki/rpm-gpg/RPM-GPG-KEY-EPEL-7
	@echo 'Install Ansible from EPEL.'
	@yum -y install ansible
	@echo 'Update Ansible configuration.'
	@sudo mkdir -p /etc/ansible/
	@sudo cp inventory.sample.conf /etc/ansible/hosts


## Provision environment using Ansible Playbooks.
provision:
	@echo 'Provisioning the localhost using Ansible Playbooks:'
	@test -e 'inventory.conf' && ( \
			echo 'Using local inventory.conf' && \
			ansible-playbook -vvv --inventory="inventory.conf" --extra-vars="project_environment=${ENVIRONMENT} hosts=${HOSTS}" main.yml \
		) || true
	@test ! -e 'inventory.conf' && ( \
			echo 'Using default inventory' && \
			ansible-playbook -vvv  --extra-vars="{project_environment: ${ENVIRONMENT}}" main.yml \
		) || true


## Provision the localhost using Ansible Playbooks.
provision-vagrant:
	@echo 'Provisioning the localhost using Ansible Playbooks:'
	@test -e 'Vagrantsettings.local.yml' && ( \
			echo 'Using Vagrantsettings.local.yml' && \
			ansible-playbook --extra-vars "@Vagrantsettings.local.yml" -b main.yml \
		) || true
	@test ! -e 'Vagrantsettings.local.yml' && ( \
			echo 'Using Vagrantsettings.yml' && \
			ansible-playbook --extra-vars "@Vagrantsettings.yml" -b main.yml \
		) || true

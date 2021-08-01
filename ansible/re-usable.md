## Ansible playbook

A playbook contains a ordered list of plays

A play contains a ordered list of tasks. Each task calls an Ansible module

```
- name: The first play
  hosts: all
  become: true
  tasks:
    - name: install nginx
      apt:
        name: nginx
        state: latest
    - name: restart nginx
      service:
        name: nginx
        state: restarted

- name: The second play
  hosts: jvm_hosts
  become: true
  tasks:
    - name: install openjdk
      apt:
        name: openjdk-8-jdk
        state: latest
```



Four distributed, re-usabl artifacts: variables, task files, playbooks, and roles

* A variables file contains only variables
* A task file contains only tasks
* 
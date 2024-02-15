import { INavData } from '@coreui/angular';

export const navItems: INavData[] = [
  {
    name: 'Dashboard',
    url: '/dashboard',
    icon: 'fa fa-chart-line'
  },
  {
    name: 'Access Rights',
    icon: 'fas fa-key',
    url: '/access',
    children: [{
      name: 'Users',
      url: '/access/users',
      icon: 'fas fa-user'
      },
      {
        name: 'Users Groupe',
        url: '/access/usersgroupe',
        icon: 'fas fa-users'
        }
    ]
  },
  {
    name: 'Functional Configuration',
    url: '/functionalconfigs',
    icon: 'fa fa-cogs',
    children: [{
      name: 'Internal Codification',
      url: '/functionalconfigs/internalcodifications',
      icon: 'fas fa-code'
      },
      {
        name: 'Free Codification',
        url: '/functionalconfigs/freecodifications',
        icon: 'fas fa-code'
        }
        ,
      {
        name: 'Campaignes ML',
        url: '/functionalconfigs/campaignesml/list',
        icon: 'fas fa-language'
        }
        ,
      {
        name: 'Applications ML',
        url: '/functionalconfigs/applicationsml/list',
        icon: 'fas fa-language'
        }
        ,
      {
        name: 'Applications Versions ML',
        url: '/functionalconfigs/appversionml/list',
        icon: 'fas fa-language'
        }
        ,
      {
        name: 'Scenarios ML',
        url: '/functionalconfigs/scenarioml/list',
        icon: 'fas fa-language'
        }
        ,
      {
        name: 'Tests ML',
        url: '/functionalconfigs/testml/list',
        icon: 'fas fa-language'
        }
    ]
  },
  {
    name: 'Campaigns',
    url: '/campaign/list',
    icon: 'fa fa-object-group'
  },
  {
    name: 'Application',
    icon: 'fa fa-cubes',
    url: '/application',
    children: [
      {
        name: 'Applications List',
        url: '/application/list',
        icon: 'fa fa-list'
      },
      {
        name: 'Versions',
        url: '/application/versions/list',
        icon: 'fa fa-code-branch',
      },
      {
        name: 'Environments',
        url: '/application/environments',
        icon: 'fab fa-envira',
      }
      ,
      {
        name: 'Traitements',
        url: '/application/traitements',
        icon: 'fa fa-briefcase',
      },
      {
        name: 'Data',
        url: '/application/data',
        icon: 'fa fa-database',
      },
    ]
  },
  {
    name: 'Scenarios',
    url: '/scenarios/list',
    icon: 'fa fa-file-video',
  },
  {
    name: 'Tests',
    url: '/tests/list',
    icon: 'fa fa-vial',
  },
  {
    name: 'Scheduling',
    url: '/scheduling',
    icon: 'fa fa-clock',
  },
   {
     name: 'Data',
     url: '/datatestcase/list',
     icon: 'fa fa-file'
   },
  // {
  //   name: 'Page',
  //   url: '/page',
  //   icon: 'fa fa-file'
  // },
  // {
  //   name: 'Page1',
  //   url: '/page1',
  //   icon: 'fa fa-file'
  // },
  {
    name: '2021 Lovotech.',
    url: 'http://www.lovotech.fr/',
    icon: 'fa fa-copyright',
    class: 'mt-auto',
    attributes: { target: '_blank', rel: 'noopener' }
  }
];

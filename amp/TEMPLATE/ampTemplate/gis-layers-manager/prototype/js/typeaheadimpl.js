var substringMatcher = function(strs) {
  return function findMatches(q, cb) {
    var matches, substrRegex;

    // an array that will be populated with substring matches
    matches = [];

    // regex used to determine if a string contains the substring `q`
    substrRegex = new RegExp(q, 'i');

    // iterate through the pool of strings and for any string that
    // contains the substring `q`, add it to the `matches` array
    $.each(strs, function(i, str) {
      if (substrRegex.test(str)) {
        // the typeahead jQuery plugin expects suggestions to a
        // JavaScript object, refer to typeahead docs for more info
        matches.push({ value: str });
      }
    });

    cb(matches);
  };
};

var project_names = ['Conscientisation à la promotion et à la protection des droits de peuples indigènes du territoire de Kabare au Sud Kivu',
'Projet de renforcement des activités de promotion du processus démocratique dans le diocèse de Kenge',
'Promotion de l égalité des genres la résolution des conflits ethniques en milieu rural',
'Equipement du secrétariat du GSRP lot 1 et N',
'Economie Solidaire et Sécurité Alimentaire pour la lutte contre la pauvreté au Kivu (ESSAIKI)',
'Renforcement des capacités des organisations paysannes pour la sécurité alimentaire dans le centre de Bandundu (RDC)',
'LAide et sécurité alimentaire des ménages vulnérables du Kasai Oriental',
'Projet de renforcément de capacités de gestion de migration en RDC.',
'Programme de mobilisation des ressources de la communauté Congolaise de l extérieur pour le développement de la RDC',
'Appui aux pisciculteurs de Ndjili Kilambu et démarrage d une exploitation porcine communautaire à Kifua.',
'Renforcement du mécanisme de survie de 85.000 familles extrêmement vulnérables dans les provinces de l Ituri, Nord et Sud-Kivu'];

$('.autocomplete .typeahead').typeahead({
  hint: true,
  highlight: true,
  minLength: 1
},
{
  name: 'project_names',
  displayKey: 'value',
  source: substringMatcher(project_names)
});
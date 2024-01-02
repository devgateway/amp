module.exports = {
  "extends": [
    "airbnb",
    "plugin:react/recommended",
    "plugin:react-hooks/recommended",
    "airbnb-typescript"
  ],
  "parser": "@typescript-eslint/parser",
  "env": {
    "browser": true,
    "mocha": true,
    "node": true
  },
  "parserOptions": {
    "project": ["./tsconfig.json" ],
    "tsconfigRootDir": __dirname,
  },
  "rules": {
    "arrow-parens": [
      "off"
    ],
    "consistent-return": "off",
    "comma-dangle": "off",
    "generator-star-spacing": "off",
    "import/extensions": "off",
    "import/no-unresolved": [
      "error",
      {
        "ignore": [
          "electron",
          "build"
        ]
      }
    ],
    "import/no-extraneous-dependencies": "off",
    "no-console": "off",
    "no-plusplus": [
      "error",
      {
        "allowForLoopAfterthoughts": true
      }
    ],
    "no-underscore-dangle": "off",
    "no-use-before-define": "off",
    "max-len": [
      "error",
      120
    ],
    "promise/param-names": 2,
    "promise/always-return": 2,
    "promise/catch-or-return": 2,
    "promise/no-native": 0,
    "react/jsx-no-bind": "off",
    "react/forbid-prop-types": 0,
    "react/no-did-mount-set-state": 2,
    "react/jsx-closing-bracket-location": 0,
    "react/jsx-filename-extension": [
      "error",
      {
        "extensions": [
          ".js",
          ".jsx",
          ".ts",
          ".tsx"
        ]
      }
    ],
    "react/prefer-stateless-function": "off",
    "no-else-return": 0,
    "no-param-reassign": 0,
    "jsx-a11y/no-static-element-interactions": "off",
    "jsx-a11y/click-events-have-key-events": "off",
    "react/no-danger": "off",
    "react/jsx-space-before-closing": "off",
    "react/jsx-tag-spacing": [
      "error",
      {
        "closingSlash": "never",
        "beforeSelfClosing": "always",
        "afterOpening": "allow-multiline"
      }
    ],
    "react/jsx-props-no-spreading": "off",
    "react/no-unstable-nested-components": "off",
    "react/jsx-curly-newline": "off",
    "react/jsx-max-props-per-line": [
      "error",
      {
        "maximum": 1
      }
    ]
  },
  "plugins": [
    "import",
    "promise",
    "react"
  ]
}
